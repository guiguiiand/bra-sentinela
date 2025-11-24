import api from './api';


export async function getReport(params) {
  const resp = await api.get('/v1/report', { params });
  return resp.data;
}


export async function getLastMonthsDetailed(months = 6) {
  const resp = await api.get('/v1/report/last-months', { params: { months } });
  return resp.data;
}


export async function downloadPdfReport(months = 6) {
  try {
    const resp = await api.get('/v1/report/last-months/pdf', {
      params: { months },
      responseType: 'blob'
    });

    const filename = `relatorio_mensal_${months}_meses.pdf`;
    downloadBlob(resp.data, filename);
  } catch (err) {
    console.error('Erro ao baixar PDF:', err);
    throw new Error('Falha ao gerar relatório PDF. Tente novamente.');
  }
}

export async function downloadCsvReport(startDate, endDate) {
  try {
    const params = {};
    if (startDate) params.start = startDate;
    if (endDate) params.end = endDate;

    console.log('📥 Baixando CSV com parâmetros:', params);

    const resp = await api.get('/v1/report/complaints/csv', {
      params,
      responseType: 'blob'
    });

    // Verifica se o response é realmente um CSV
    const contentType = resp.headers['content-type'];
    console.log('📄 Content-Type recebido:', contentType);

    if (contentType && contentType.includes('application/json')) {
      // Se retornou JSON, é um erro
      const text = await resp.data.text();
      const error = JSON.parse(text);
      throw new Error(error.message || 'Erro ao gerar CSV');
    }

    // Gera nome do arquivo
    const start = startDate || 'inicio';
    const end = endDate || 'hoje';
    const filename = `denuncias_${start}_ate_${end}.csv`;

    downloadBlob(resp.data, filename);
  } catch (err) {
    console.error('Erro ao baixar CSV:', err);
    throw new Error(err.message || 'Falha ao gerar CSV. Tente novamente.');
  }
}


function downloadBlob(blob, filename) {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', filename);
  document.body.appendChild(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
  console.log('✅ Download iniciado:', filename);
}

export async function downloadReport(reportParams) {
  const { type, start, end } = reportParams || {};

  switch (type) {
    case 'resumo_mensal':
      let months = 6;
      if (start && end) {
        try {
          const startDate = new Date(start);
          const endDate = new Date(end);
          const diffMs = Math.abs(endDate - startDate);
          months = Math.max(1, Math.ceil(diffMs / (30 * 24 * 60 * 60 * 1000)));
        } catch (err) {
          console.warn('Erro ao calcular meses, usando 6:', err);
        }
      }
      await downloadPdfReport(months);
      break;

    case 'export_csv':
      await downloadCsvReport(start, end);
      break;

    case 'por_canal':
    case 'status_validacao':
      throw new Error('Tipo de relatório ainda não implementado no front-end');

    default:
      throw new Error('Tipo de relatório não suportado');
  }
}

export default {
  getReport,
  getLastMonthsDetailed,
  downloadReport,
  downloadCsvReport,
  downloadPdfReport
};